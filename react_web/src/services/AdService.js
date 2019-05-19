import request from '../utils/request';

const HTTP_URL = "http://192.168.100.223:9098"
//根据日期查询类目访问排行
const getCateListByDate = params => {
  return request(`${HTTP_URL}/cate/listbydate?date=${params.date}`)
}
/**
 * 获取留存分析
 * @param {date: '2017-04-22', id:123} params 
 */
const getCateFunnel = params => {
  return request(`${HTTP_URL}/funnel/get`,{
    method: 'post',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
    body: JSON.stringify(params)
  })
}
/**
 * 获取所有类目ID
 */
const getCateIds = () => {
  return request(`${HTTP_URL}/funnel/cateids`)
}

/**
 * 根据rowKey获取留存信息
 * @param {*} params 
 */
const getRetainsByRow = params => {
  return request(`${HTTP_URL}/retention/get?row=${params.row}`)
}


const getAgeDistributedByCate = params => {
  return request(`${HTTP_URL}/distributed/age?cate=${params.cateId}`)
}

const getGenderDistributedByCate = params => {
  return request(`${HTTP_URL}/distributed/gender?cate=${params.cateId}`)
}

const getPvalueDistributedByCate = params => {
  return request(`${HTTP_URL}/distributed/pvalue?cate=${params.cateId}`)
}

export {getCateListByDate, getCateFunnel, getCateIds, getRetainsByRow, getAgeDistributedByCate, getGenderDistributedByCate, getPvalueDistributedByCate}
