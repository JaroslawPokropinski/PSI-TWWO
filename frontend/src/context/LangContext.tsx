import React, { useState } from 'react';
import { IntlProvider } from 'react-intl';
import locales from '../shared/locales';

export const LangContext = React.createContext<{
  locale: string;
  messages: Record<string, string>;
  selectLanguage: (lang: string) => void;
}>({
  locale: 'en',
  messages: {},
  selectLanguage: () => {},
});
const local = locales[navigator.language] ? navigator.language : 'en';

const LanguageProvider: React.FunctionComponent = ({ children = [] }) => {
  const [locale, setLocale] = useState(local);
  const [messages, setMessages] = useState(locales[local]);

  function selectLanguage(lang: string) {
    setLocale(locales[lang] ? lang : 'en');
    setMessages(locales[lang] ?? locales.en);
  }

  return (
    <LangContext.Provider value={{ locale, messages, selectLanguage }}>
      <IntlProvider messages={messages} locale={locale}>
        {children}
      </IntlProvider>
    </LangContext.Provider>
  );
};
export default LanguageProvider;
