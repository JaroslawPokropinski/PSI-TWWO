import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Form } from 'antd';
import { useHistory } from 'react-router-dom';
import AuthContext from '../context/AuthContext';
import { PagedResult } from '../shared/PagedResult';
import { PAGE_SIZE } from '../configuration/constants';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';
import PagedPickTable from '../shared/PagedPickTable';
import { Program } from '../dto/Program';

type ProgramPickerProps = {
  modify: boolean;
  initPrograms?: Program[];
};

export const ProgramPicker: React.FunctionComponent<ProgramPickerProps> = ({
  modify = false,
  initPrograms = [],
}) => {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [pages, setPages] = useState<PagedResult<Program> | null>(null);
  const [value, setValue] = useState('');

  const changePage = useCallback(
    (page: unknown, filter: string) => {
      axios
        .get<PagedResult<Program>>(
          `/api/studies-program/api/studies-program?page=${page}&size=${PAGE_SIZE}&query=${encodeURIComponent(
            `code=ke="${filter}"`
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
    changePage(0, value);
  }, [changePage, value]);

  // useEffect(() => console.log(initPrograms), [initPrograms]);

  return (
    <>
      {pages == null ? null : (
        <Form.Item name="studiesProgramId">
          <PagedPickTable
            type="radio"
            changePage={changePage}
            dataSource={pages.results.map((e) => ({
              ...e,
              value: e.code,
            }))}
            modify={modify}
            initVals={initPrograms.map((e) => ({
              ...e,
              value: e.code,
            }))}
            onSearch={(f) => setValue(f)}
            columns={[
              {
                title: 'Code',
                dataIndex: 'code',
              },
            ]}
          />
        </Form.Item>
      )}
    </>
  );
};
