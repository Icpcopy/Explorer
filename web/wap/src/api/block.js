import request from '@/utils/request'


export function getBlockList(data) {
  return request({
    url: '/api/block/getBlockList',
    method: 'get',
    params:data
  })
}
export function getBlockByBlockNumber(data) {
    return request({
      url: '/api/block/getBlockByBlockNumber',
      method: 'get',
      params:data
    })
  }
  export function getAllTypeCount(data) {
    return request({
      url: '/api/transaction/getAllTypeCount',
      method: 'get',
      params:data
    })
  }
  export function getTransactionByBlockNumber(data) {
    return request({
      url: '/api/transaction/getTransactionByBlockNumber',
      method: 'get',
      params:data
    })
  }
  
