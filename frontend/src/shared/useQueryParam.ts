// useQueryParam.ts

import { useState } from 'react';

const getQuery = () => {
  if (typeof window !== 'undefined') {
    if (window.location.hash !== '') {
      const search = window.location.hash.split('?')[1] ?? '';
      return new URLSearchParams(search);
    }
    return new URLSearchParams(window.location.search);
  }
  return new URLSearchParams();
};

const getQueryStringVal = (key: string): string | null => {
  return getQuery().get(key);
};

const useQueryParam = (
  key: string,
  defaultVal: string | undefined = ''
): [string, (val: string) => void] => {
  const [query, setQuery] = useState(getQueryStringVal(key) || defaultVal);

  const updateUrl = (newVal: string) => {
    setQuery(newVal);

    const nquery = getQuery();

    if (newVal.trim() !== '') {
      nquery.set(key, newVal);
    } else {
      nquery.delete(key);
    }

    // This check is necessary if using the hook with Gatsby
    if (typeof window !== 'undefined') {
      const { protocol, pathname, host } = window.location;
      const newUrl = `${protocol}//${host}${pathname}?${nquery.toString()}`;
      window.history.pushState({}, '', newUrl);
    }
  };

  return [query, updateUrl];
};

export default useQueryParam;
