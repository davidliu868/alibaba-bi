import { getAgeDistributedByCate, getGenderDistributedByCate, getPvalueDistributedByCate} from '../services/AdService'

export default {
    namespace: 'cateDistributed',

    state: {
        ages: [
            {
                age: '1',
                count: 10
            }
        ],
        genders: [
            {
                age: '1',
                count: 10
            }
        ],
        pvalues: [
            {
                age: '1',
                count: 10
            }
        ],
    },

    subscriptions: {
        setup({ dispatch, history }) {  // eslint-disable-line
        },
    },

    effects: {
        *fetchAges({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getAgeDistributedByCate, payload)
            yield put({
                type: 'saveAges',
                payload: {
                    ages: data
                }
            });
        },

        *fetchGenders({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getGenderDistributedByCate, payload)
            yield put({
                type: 'saveGenders',
                payload: {
                    genders: data
                }
            });
        },

        *fetchPvalues({ payload }, { call, put }) {  // eslint-disable-line
            const {data} = yield call(getPvalueDistributedByCate, payload)
            yield put({
                type: 'savePvalues',
                payload: {
                    pvalues: data
                }
            });
        },
        
    },

    reducers: {
        saveAges(state, {payload}) {
            return { ...state, ages: payload.ages};
        },
        saveGenders(state, {payload}) {
            return { ...state, genders: payload.genders};
        },
        savePvalues(state, {payload}) {
            return { ...state, pvalues: payload.pvalues};
        },
       
    },
}