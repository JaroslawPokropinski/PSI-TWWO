import { Card as AntCard, Checkbox, Form, Input, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import fileDownload from 'js-file-download';

import './CardsEditor.css';
import { useForm } from 'antd/lib/form/Form';
import useQueryParam from '../shared/useQueryParam';
import EditorView from '../shared/EditorView';
import CardGoals from './CardGoals';
import CardEffects from './CardEffects';
import CardRequirements from './CardRequirements';
import CardTools from './CardTools';
import CardClasses from './CardClasses';
import CardLiterature from './CardLiterature';
import CardSecondaryLiterature from './CardSecondaryLiterature';
import { Card } from '../dto/Card';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';
import { Effect } from '../dto/Effect';
import { VersionHistory } from '../shared/versionHistory';
import VerificationStatus from '../shared/VeryficationStatus';

type FieldOfStudy = { id: number; name: string; faculty: number };
type OrganisationalUnit = { id: number; name: string; type: string };

const CardsEditorContent = ({
  isArchive = false,
  effects = new Array<Effect>(),
}) => {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  const [id] = useQueryParam('id');

  const [fields, setFields] = useState<FieldOfStudy[] | null>(null);
  const [units, setUnits] = useState<OrganisationalUnit[] | null>(null);

  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  useEffect(() => {
    axios
      .get<OrganisationalUnit[]>(`/api/organisational-units`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setUnits(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [setUnits, auth, history]);

  useEffect(() => {
    axios
      .get<FieldOfStudy[]>(`/api/field-of-study`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setFields(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [setFields, auth, history]);

  return (
    <>
      {fields == null || units == null ? null : (
        <>
          <AntCard>
            <VerificationStatus
              modify={modify}
              label={lang.getMessage('State')}
            />
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Subject code')}
              labelAlign="left"
              name="subjectCode"
              rules={[
                {
                  required: true,
                  message: lang.getMessage('Fill subject code!'),
                },
              ]}
            >
              <Input disabled={!(state === 'edit' && id === '')} />
            </Form.Item>
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Subject name')}
              labelAlign="left"
              name="subjectName"
              rules={[
                {
                  required: true,
                  message: lang.getMessage('Fill subject name!'),
                },
              ]}
            >
              <Input disabled={!(state === 'edit' && id === '')} />
            </Form.Item>
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Subject name in english')}
              labelAlign="left"
              name="subjectNameInEnglish"
              rules={[
                {
                  required: true,
                  message: lang.getMessage('Fill subject name in english!'),
                },
              ]}
            >
              <Input disabled={!modify} />
            </Form.Item>
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Specialization')}
              labelAlign="left"
              name="specialization"
              rules={[
                {
                  required: false,
                  message: lang.getMessage('Fill specialization!'),
                },
              ]}
            >
              <Input disabled={!modify} />
            </Form.Item>
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Field of study2')}
              labelAlign="left"
              name="fieldOfStudy"
              rules={[
                {
                  required: true,
                  message: lang.getMessage('Fill field of study!'),
                },
              ]}
            >
              <Select
                placeholder={lang.getMessage('Fill field of study!')}
                disabled={!modify}
              >
                {fields.map((f) => (
                  <Select.Option key={f.id} value={f.id}>
                    {f.name}
                  </Select.Option>
                ))}
              </Select>
            </Form.Item>
            <Form.Item
              className="cards-form-item"
              label={lang.getMessage('Organisational unit')}
              labelAlign="left"
              name="organisationalUnit"
              rules={[
                {
                  required: true,
                  message: lang.getMessage('Fill organisationel unit!'),
                },
              ]}
            >
              <Select
                placeholder={lang.getMessage('Fill organisationel unit!')}
                disabled={!modify}
              >
                {units.map((u) => (
                  <Select.Option key={u.id} value={u.id}>
                    {u.name}
                  </Select.Option>
                ))}
              </Select>
            </Form.Item>
            {/* rodzajPrzedmiotu */}
            <Form.Item
              className="form-item"
              label="Rodzaj przedmiotu"
              labelAlign="left"
              name="subjectType"
              rules={[
                {
                  required: true,
                  message: 'Wprowadź rodzaj przedmiotu!',
                },
              ]}
            >
              <Select
                placeholder="Wprowadź rodzaj przedmiotu"
                disabled={!modify}
              >
                <Select.Option value="OBLIGATORY">Obowiązkowy</Select.Option>
                <Select.Option value="OPTIONAL">Wybieralny</Select.Option>
                <Select.Option value="UNIVERSITY_WIDE">
                  Ogólnouczelniany
                </Select.Option>
              </Select>
            </Form.Item>
            {/* formaStudiów */}
            <Form.Item
              name="studiesForm"
              label="Forma studiów"
              labelAlign="left"
              hasFeedback
              rules={[{ required: true, message: 'Wybierz forme studiów!' }]}
            >
              <Select placeholder="Wybierz forme studiów" disabled={!modify}>
                <Select.Option value="FULL_TIME">Stacjonarne</Select.Option>
                <Select.Option value="PART_TIME">Nie stacjonarne</Select.Option>
              </Select>
            </Form.Item>
            {/* stopień */}
            <Form.Item
              name="studiesLevel"
              label="Stopień"
              labelAlign="left"
              hasFeedback
              rules={[{ required: true, message: 'Wybierz stopień studiów!' }]}
            >
              <Select placeholder="Wybierz stopień studiów" disabled={!modify}>
                <Select.Option value="FIRST">Pierwszego stopnia</Select.Option>
                <Select.Option value="SECOND">Drugiego stopnia</Select.Option>
                <Select.Option value="UNIFORM_MAGISTER_STUDIES">
                  Jednolite magisterskie
                </Select.Option>
              </Select>
            </Form.Item>
            {/* czyGrupaKursów */}
            <Form.Item
              className="form-item"
              name="isGroupOfCourses"
              valuePropName="checked"
            >
              <Checkbox disabled={!modify}>Czy jest grupą kursów</Checkbox>
            </Form.Item>
          </AntCard>
          {/* Adding descriptions */}
          <CardGoals modify={modify} />
          {/* Adding effects */}
          <AntCard title={lang.getMessage('Studies effects')}>
            <CardEffects modify={modify} initEffects={effects} />
          </AntCard>
          {/* wymaganieWstępne */}
          <AntCard title={lang.getMessage('Card requirements')}>
            <CardRequirements modify={modify} />
          </AntCard>
          {/* narzędziaDydaktyczne */}
          <AntCard title={lang.getMessage('Tools')}>
            <CardTools modify={modify} />
          </AntCard>
          {/* Zajęcia */}
          <AntCard title={lang.getMessage('Classes')}>
            <CardClasses modify={modify} />
          </AntCard>
          {/* literatura */}
          <CardLiterature modify={modify} />
          {/* */}
          <CardSecondaryLiterature modify={modify} />
        </>
      )}
    </>
  );
};

function CardsEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [card, setCard] = useState<Card | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );
  const [form] = useForm();

  const onFinish = useCallback(
    (d) => {
      const newCard = {
        code: d.subjectCode,
        ...d,
        isGroupOfCourses: d.isGroupOfCourses ?? false,
        educationalEffects: d.educationalEffects ?? [],
        prerequisites: d.prerequisites ?? [],
        primaryLiterature: d.primaryLiterature ?? [],
        secondaryLiterature: d.secondaryLiterature ?? [],
        subjectObjectives: d.subjectObjectives ?? [],
        usedTeachingTools: d.usedTeachingTools ?? [],
        subjectClasses:
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          (d.subjectClasses as any[])?.map((c) => ({
            ...c,
            isFinalCourse: c.isFinalCourse ?? false,
          })) ?? [],
      };
      axios
        .get('/api/user/current', axiosOpts)
        .then((res) => {
          newCard.supervisor = res.data.id;
          return isNew || card == null
            ? axios.post(`/api/subject-card`, [newCard], axiosOpts)
            : axios.put(
                `/api/subject-card`,
                [{ id: card.id, ...newCard }],
                axiosOpts
              );
        })
        .then((res) =>
          axios.get<Card[]>(`/api/subject-card/${res.data[0].id}`, axiosOpts)
        )
        .then((res) => {
          setCard(res.data[0]);
          form.resetFields();
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history, card, isNew, form]
  );

  const onDownload = useCallback(() => {
    if (id == null) return;

    axios
      .get(`/api/subject-card/download/${id}?lang=${lang.locale}`, {
        ...axiosOpts,
        responseType: 'blob',
      })

      .then((res) => {
        const filename: string =
          res.headers?.filename ??
          (card != null
            ? `${card.subjectCode} - ${card.subjectName}.pdf`
            : `${id}.pdf`);

        fileDownload(res.data, filename);
      })
      .catch((e) => handleHttpError(e));
  }, [id, axiosOpts, lang, card]);

  const onDownloadOpt = useMemo(() => (id == null ? undefined : onDownload), [
    id,
    onDownload,
  ]);

  useEffect(() => {
    if (isNew) return;
    axios
      .get<Card[]>(`/api/subject-card/${id}`, axiosOpts)
      .then((res) => {
        setCard(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const [archiveVals, setArchiveVals] = useState<VersionHistory<Card> | null>(
    null
  );

  useEffect(() => {
    if (isNew) return;

    const versionHistory = new VersionHistory<Card>(
      '/api/subject-card/history',
      id,
      { headers: { Authorization: auth.token } }
    );
    versionHistory
      .init()
      .then(() => {
        setArchiveVals(versionHistory);
      })
      .catch((e) => handleHttpError(e, history));
  }, [isNew, id, auth, history]);

  const mapCard = useCallback((c: Card) => {
    try {
      return {
        ...c,
        organisationalUnit: c.organisationalUnit.id,
        fieldOfStudy: c.fieldOfStudy.id,
        educationalEffects: c.educationalEffects.map((e) => e.id),
      };
    } catch (e) {
      return {};
    }
  }, []);

  const onVerify = useCallback(() => {
    if (isNew) return;
    axios
      .patch(
        `/api/subject-card/status/${id}`,
        { status: 'VERIFIED' },
        {
          headers: { Authorization: auth.token },
        }
      )
      .then(() => axios.get<Card[]>(`/api/subject-card/${id}`, axiosOpts))
      .then((res) => {
        setCard(res.data[0]);
        form.resetFields();
      })
      .catch((err) => handleHttpError(err));
  }, [id, auth, form, axiosOpts, isNew]);

  const onRemove = useCallback(() => {
    axios.delete(`/api/subject-card/${id}`, {
      headers: { Authorization: auth.token },
    });
  }, [id, auth]);

  return (
    <div className="cards-editor">
      {card == null && !isNew ? null : (
        <EditorView
          name="cards"
          mapper={mapCard}
          initialVals={card == null ? {} : card}
          onFinish={onFinish}
          queryParams=""
          header={lang.getMessage('Subject card')}
          isVerifiable={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          isVerified={card?.objectState === 'VERIFIED'}
          useArchive
          isAllowedToEdit={[
            'ROLE_ADMIN',
            'ROLE_COMMISSION_MEMBER',
            'ROLE_SUBJECT_SUPERVISOR',
          ].includes(auth.role)}
          versionHistory={archiveVals}
          onDownload={onDownloadOpt}
          form={form}
          onVerify={onVerify}
          onRemove={onRemove}
        >
          <CardsEditorContent effects={card?.educationalEffects} />
          <CardsEditorContent
            isArchive
            effects={archiveVals?.page?.results?.flatMap(
              (r) => r.entity.educationalEffects
            )}
          />
        </EditorView>
      )}
    </div>
  );
}

export default CardsEditor;
