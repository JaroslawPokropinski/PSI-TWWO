import React from 'react';

type AuthContextType = {
  token: string | null;
};
const AuthContext = React.createContext<AuthContextType>({
  token: null,
});

export default AuthContext;
