import React, { useState } from 'react';
import { IntlProvider } from 'react-intl';
import locales from '../shared/locales';

// eslint-disable-next-line no-console
const consoleError = console.error.bind(console);
// eslint-disable-next-line
console.error = (message: any, ...args: unknown[]) => {
  if (
    message
      .toString()
      .startsWith('Error: [@formatjs/intl Error MISSING_TRANSLATION]')
  ) {
    return;
  }
  consoleError(message, ...args);
};

export const LangContext = React.createContext<{
  locale: string;
  messages: Record<string, string>;
  selectLanguage: (lang: string) => void;
  getMessage: (message: string) => string;
}>({
  locale: 'en',
  messages: {},
  selectLanguage: () => {},
  getMessage: (v) => v,
});
const local = locales[navigator.language] ? navigator.language : 'en';

const LanguageProvider: React.FunctionComponent = ({ children = [] }) => {
  const [locale, setLocale] = useState(local);
  const [messages, setMessages] = useState(locales[local]);

  function selectLanguage(lang: string) {
    setLocale(locales[lang] ? lang : 'en');
    setMessages(locales[lang] ?? locales.en);
  }

  function getMessage(message: string) {
    return messages[message] ?? message;
  }

  return (
    <LangContext.Provider
      value={{ locale, messages, selectLanguage, getMessage }}
    >
      <IntlProvider messages={messages} locale={locale}>
        {children}
      </IntlProvider>
    </LangContext.Provider>
  );
};
export default LanguageProvider;
