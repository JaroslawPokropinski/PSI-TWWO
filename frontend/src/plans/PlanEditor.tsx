import { Card as AntCard, DatePicker, Form, Input } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import moment from 'moment';
import { useForm } from 'antd/lib/form/Form';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import EditorView from '../shared/EditorView';
import handleHttpError from '../shared/handleHttpError';
import useQueryParam from '../shared/useQueryParam';

import { Plan } from '../dto/Plan';
import { SemesterEditor } from './SemesterEditor';
import { Card } from '../dto/Card';
import { ProgramPicker } from './ProgramPicker';
import { Program } from '../dto/Program';
import { Semester } from '../dto/Semester';
import VerificationStatus from '../shared/VeryficationStatus';

const ProgramsEditorContent = ({
  isArchive = false,
  initCards = new Array<Card>(),
  initPrograms = new Array<Program>(),
}) => {
  const lang = useContext(LangContext);

  const { state } = useParams<{ state: string }>();

  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  return (
    <>
      <AntCard>
        <VerificationStatus modify={modify} label={lang.getMessage('State')} />
        <Form.Item
          className="form-item"
          label="Kod"
          labelAlign="left"
          name="code"
          rules={[
            { required: true, message: 'Wprowadź kod programu uczenia!' },
          ]}
        >
          <Input disabled={!(state === 'edit')} />
        </Form.Item>

        <Form.Item
          className="cards-form-item"
          label={lang.getMessage('In effect since')}
          labelAlign="left"
          name="inEffectSince"
          rules={[
            {
              required: true,
              message: 'Wprowadź jednostkę organizacyjną (wydział)!',
            },
          ]}
        >
          <DatePicker />
        </Form.Item>
      </AntCard>
      <AntCard title={lang.getMessage('Program')}>
        <ProgramPicker modify={modify} initPrograms={initPrograms} />
      </AntCard>
      <AntCard title={lang.getMessage('Semesters')}>
        <SemesterEditor modify={modify} initCards={initCards} />
      </AntCard>
    </>
  );
};

function PlanEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [plan, setPlan] = useState<Plan | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );
  const [form] = useForm();

  const onFinish = useCallback(
    (d) => {
      const newPlan = {
        ...d,
        decreeDate: moment.utc().format(),
        studiesProgramId: d.studiesProgramId[0],
      };
      (isNew || plan == null
        ? axios.post(`/api/studies-plan`, [newPlan], axiosOpts)
        : axios.put(
            `/api/studies-plan`,
            [{ id: plan.id, ...newPlan }],
            axiosOpts
          )
      )
        .then((res) =>
          axios.get<Plan[]>(`/api/studies-plan/${res.data[0].id}`, axiosOpts)
        )
        .then((res) => {
          setPlan(res.data[0]);
          return axios.post(
            `/api/semester`,
            d.semesters.map((s: Record<string, unknown>, n: number) => ({
              ...s,
              number: n + 1,
              studiesPlanId: res.data[0].id,
            })),
            axiosOpts
          );
        })
        .then(() => {
          form.resetFields();
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history, plan, isNew, form]
  );

  useEffect(() => {
    if (isNew) return;
    axios
      .get<Plan[]>(`/api/studies-plan/${id}`, axiosOpts)
      .then((res) => {
        setPlan(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const [initPrograms, setInitPrograms] = useState<Program[] | null>([]);
  useEffect(() => {
    if (isNew || plan == null) return;
    axios
      .get<Program[]>(
        `/api/studies-program/${plan.studiesProgramId}`,
        axiosOpts
      )
      .then((res) => {
        setInitPrograms(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts, plan]);

  const [semesters, setSemesters] = useState<Semester[] | null>(null);
  useEffect(() => {
    if (isNew) return;
    axios
      .get<Semester[]>(`/api/semester/studies-plan/${id}`, axiosOpts)
      .then((res) => {
        setSemesters(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const mapPlan = useCallback(
    (p: Plan) => {
      try {
        return {
          ...p,
          studiesProgramId: [p.studiesProgramId],
          inEffectSince: moment(p.inEffectSince),
          semesters,
        };
      } catch (e) {
        return {};
      }
    },
    [semesters]
  );

  const onVerify = useCallback(() => {
    axios
      .patch(
        `/api/studies-plan/status/${id}`,
        { status: 'VERIFIED' },
        {
          headers: { Authorization: auth.token },
        }
      )
      .then(() => axios.get<Plan[]>(`/api/studies-plan/${id}`, axiosOpts))
      .then((res) => {
        setPlan(res.data[0]);
        form.resetFields();
      })
      .catch((err) => handleHttpError(err));
  }, [id, auth, form, axiosOpts]);

  const onRemove = useCallback(() => {
    axios.delete(`/api/studies-program/${id}`, {
      headers: { Authorization: auth.token },
    });
  }, [id, auth]);

  const [cards, setCards] = useState<Card[] | null>(null);
  useEffect(() => {
    if (semesters == null) return;

    const cardIds = semesters.flatMap((s) => s.subjectCardIds);
    if (cardIds.length === 0) {
      setCards([]);
      return;
    }

    axios
      .get<Card[]>(`/api/subject-card/${cardIds.join(',')}`, axiosOpts)
      .then((res) => {
        setCards(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [semesters, history, axiosOpts]);

  return (
    <div>
      {(plan == null ||
        initPrograms == null ||
        semesters == null ||
        cards == null) &&
      !isNew ? null : (
        <EditorView
          name="plans"
          mapper={mapPlan}
          initialVals={plan ?? {}}
          onFinish={onFinish}
          queryParams=""
          header="Plany studiów"
          isVerifiable={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          isAllowedToEdit={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          isVerified={plan?.objectState === 'VERIFIED'}
          form={form}
          onVerify={onVerify}
          onRemove={onRemove}
        >
          <ProgramsEditorContent
            initCards={cards ?? []}
            initPrograms={initPrograms ?? []}
          />
        </EditorView>
      )}
    </div>
  );
}

export default PlanEditor;
