// utils/auth.js
import jwtDecode from 'jwt-decode';

export function getCurrentUser() {
  const token = localStorage.getItem('token');
  if (!token) return null;

  try {
    const decoded = jwtDecode(token);
    return decoded.sub; // 后端用 setSubject(username)，这里用 sub 获取用户名
  } catch (error) {
    return null;
  }
}
