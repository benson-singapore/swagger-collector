import { request } from 'umi';

export async function query() {
  return request<API.CurrentUser[]>('/api/users');
}

export async function queryCurrent() {
  return request<API.ResultPoJo<API.CurrentUser>>('/swagger/api/currentUser');
}

export async function queryNotices(): Promise<any> {
  return request<{ data: API.NoticeIconData[] }>('/api/notices');
}

export async function loginVerify() {
  return request<API.ResultPoJo<boolean>>('/swagger/login/verify');
}
