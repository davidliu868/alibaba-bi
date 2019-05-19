import { getCateFunnel, getCateIds} from '../services/AdService'

export default {
    namespace: 'cateFunnel',

    state: {
        funnels: [
            {
                id: 1,
                action: '喜欢',
                pv: 123
            }
        ],//留存信息
        cateIds: [],
    },

    subscriptions: {
        setup({ dispatch, history }) {  // eslint-disable-line
        },
    },

    effects: {
        *fetchFunnels({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getCateFunnel, payload)
            yield put({
                type: 'save',
                payload: {
                    funnels: data
                }
            });
        },
        *fetchCateIds( _ , { call, put }) {  // eslint-disable-line
            const { data } = yield call(getCateIds)
            yield put({
                type: 'saveCateIds',
                payload: {
                    cateIds: data
                }
            });
        },
    },

    reducers: {
        save(state, {payload}) {
            return { ...state, funnels: payload.funnels};
        },
        saveCateIds(state, {payload}) {
            return { ...state, cateIds: payload.cateIds};
        },
    },
}