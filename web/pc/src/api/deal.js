import request from '@/utils/request'
export function getByBlockNumber(data) {
    return request({
      url: '/api/transaction/getTransactionByBlockNumber',
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
  export function getBlockList(data) {
    return request({
      url: '/api/block/getBlockList',
      method: 'get',
      params:data
    })
  }
  export function getTransactionByHash(data) {
    return request({
      url: '/api/transaction/getTransactionByHash',
      method: 'get',
      params:data
    })
  }
  export function getAddressDelegateInfo(data) {
    return request({
      url: '/api/address/getAddressDelegateInfo',
      method: 'get',
      params:data
    })
  }