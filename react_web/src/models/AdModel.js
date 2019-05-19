export default {

  namespace: 'adDatas',

  state: {
    name: 'Ali-Bi-广告项目',
    date: '2017-04-22',
    cates: [],//类目点击量排行
  },

  subscriptions: {
    setup({ dispatch, history }) {  // eslint-disable-line
    },
  },

  effects: {
    *fetch({ payload }, { call, put }) {  // eslint-disable-line
      yield put({ type: 'save' });
    },
  },

  reducers: {
    save(state, action) {
      return { ...state, ...action.payload };
    },
  },

};
