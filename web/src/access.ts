// src/access.ts
export default function access(initialState: { currentUser: API.CurrentUser | undefined, isLogin: boolean | false }) {
  const { currentUser, isLogin} = initialState || {};
  // currentUser && currentUser.access === 'admin',
  return {
    canAdmin: isLogin,
  };
}
