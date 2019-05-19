import { getCateListByDate } from '../services/AdService'

export default {
    namespace: 'cateTable',

    state: {
        cates: [],//类目信息
    },

    subscriptions: {
        setup({ dispatch, history }) {  // eslint-disable-line
        },
    },

    effects: {
        *fetchCates({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getCateListByDate, payload)
            yield put({
                type: 'save',
                payload: {
                    cates: data.cates
                }
            });
        },
    },

    reducers: {
        save(state, {payload}) {
            return { ...state, cates: payload.cates};
        },
    },
}