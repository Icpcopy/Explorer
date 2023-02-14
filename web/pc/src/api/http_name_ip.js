import request from '@/utils/request'
export function getChainName(data) {
    return request({
      url: '/api/chain/getChainName',
      method: 'get',
      params:data
    })
  }
