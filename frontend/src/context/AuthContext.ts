import React from 'react';

type AuthContextType = {
  token: string | null;
  role:
    | 'ROLE_USER'
    | 'ROLE_SUBJECT_SUPERVISOR'
    | 'ROLE_COMMISSION_MEMBER'
    | 'ROLE_ADMIN';
};
const AuthContext = React.createContext<AuthContextType>({
  token: null,
  role: 'ROLE_USER',
});

export default AuthContext;
