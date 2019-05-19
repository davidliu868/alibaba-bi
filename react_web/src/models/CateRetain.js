import { getRetainsByRow} from '../services/AdService'

export default {
    namespace: 'cateRetain',

    state: {
        retains: [
            {
                date: "2019-01-01",
                value: 100
            },
        ],//留存分析
    },

    subscriptions: {
        setup({ dispatch, history }) {  // eslint-disable-line
        },
    },

    effects: {
        *fetchRetains({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getRetainsByRow, payload)
            yield put({
                type: 'save',
                payload: {
                    retains: data
                }
            });
        },
    },

    reducers: {
        save(state, {payload}) {
            return { ...state, retains: payload.retains};
        },
    },
}