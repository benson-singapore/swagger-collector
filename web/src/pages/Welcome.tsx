import React from 'react';
import { history } from 'umi';

// 默认跳转
export default (): React.ReactNode => {
  history.push('/api/swagger');
  return <span></span>;
};
