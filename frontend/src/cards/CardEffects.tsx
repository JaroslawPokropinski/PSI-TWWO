import React, { useCallback, useContext, useEffect, useState } from 'react';
import { AutoComplete, Button, Card, Form, Select } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useHistory } from 'react-router-dom';
import axios from '../configuration/axios';
import { PAGE_SIZE } from '../configuration/constants';
import { PagedResult } from '../shared/PagedResult';
import { Effect } from '../dto/Effect';
import AuthContext from '../context/AuthContext';
import handleHttpError from '../shared/handleHttpError';
import PagedPickTable from '../shared/PagedPickTable';

const CardEffects: React.FunctionComponent<{
  modify: boolean;
  initEffects?: Effect[];
}> = ({ modify = false, initEffects = [] }) => {
  const auth = useContext(AuthContext);
  // const lang = useContext(LangContext);

  const history = useHistory();

  const [chosen, setChosen] = useState<Effect[]>(initEffects);

  const [effects, setEffects] = useState<Effect[] | null>();
  const [value, setValue] = useState('');
  const [disabled, setDisabled] = useState(true);

  useEffect(() => {
    axios
      .get<PagedResult<Effect>>(
        `/api/educational-effects/search?page=0&size=${PAGE_SIZE}&query=${encodeURIComponent(
          `code=ke="${value}" or description=ke="${value}"`
        )}`,
        {
          headers: { Authorization: auth.token },
        }
      )
      .then((res) => {
        setEffects(res.data.results);
      })
      .catch((err) => handleHttpError(err, history));
  }, [auth, history, value]);

  // useEffect(() => {
  //   if (effects == null) return;
  //   setDisabled(effects.find((e) => e.description === value) == null);
  // }, [effects, value]);

  // const onAdd = useCallback(
  //   (add: (e: number) => void) => {
  //     if (effects == null) return;

  //     const newElem = effects.find((e) => e.description === value);
  //     if (newElem == null) return;

  //     add(newElem.id);

  //     setChosen([...chosen, newElem]);
  //   },
  //   [effects, chosen, value]
  // );

  const [pages, setPages] = useState<PagedResult<Effect> | null>(null);
  const changePage = useCallback(
    (filter) => {
      axios
        .get<PagedResult<Effect>>(
          `/api/educational-effects/search?page=0&size=${PAGE_SIZE}&query=${encodeURIComponent(
            `code=ke="${filter}" or description=ke="${filter}"`
          )}`,
          {
            headers: { Authorization: auth.token },
          }
        )
        .then((res) => {
          setPages(res.data);
        })
        .catch((err) => handleHttpError(err, history));
    },
    [auth, history]
  );

  useEffect(() => {
    changePage(0);
  }, [changePage]);

  return (
    <>
      {effects == null ? null : (
        <Form.Item name="educationalEffects">
          <PagedPickTable
            changePage={changePage}
            dataSource={effects.map((e) => ({
              id: e.id,
              value: e.description,
            }))}
            modify={modify}
            initVals={initEffects.map((e) => ({
              id: e.id,
              value: e.description,
            }))}
            onSearch={(f) => setValue(f)}
          />
        </Form.Item>
      )}
    </>
  );
};

export default CardEffects;
