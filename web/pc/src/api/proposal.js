import request from '@/utils/request'
export function getProposalIdList(data) {
    return request({
      url: '/api/proposal/getProposalIdList',
      method: 'get',
      params:data
    })
  }
  export function getProposalByProposalId(data) {
    return request({
      url: '/api/proposal/getProposalByProposalId',
      method: 'get',
      params:data
    })
  }
