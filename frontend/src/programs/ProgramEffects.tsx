import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Form } from 'antd';
import { useHistory } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { Effect } from '../dto/Effect';
import { PagedResult } from '../shared/PagedResult';
import { PAGE_SIZE } from '../configuration/constants';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';
import PagedPickTable from '../shared/PagedPickTable';

const ProgramEffects: React.FunctionComponent<{
  modify: boolean;
  initEffects?: Effect[];
}> = ({ modify = false, initEffects = [] }) => {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [effects, setEffects] = useState<Effect[] | null>();
  const [, setPages] = useState<PagedResult<Effect> | null>(null);
  const [value, setValue] = useState('');

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
              ...e,
              value: e.description,
            }))}
            modify={modify}
            initVals={initEffects.map((e) => ({
              ...e,
              value: e.description,
            }))}
            onSearch={(f) => setValue(f)}
            columns={[
              {
                title: 'Code',
                dataIndex: 'code',
              },
              {
                title: 'Description',
                dataIndex: 'description',
              },
            ]}
          />
        </Form.Item>
      )}
    </>
  );
};
export default ProgramEffects;
