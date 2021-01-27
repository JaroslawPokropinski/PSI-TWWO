import { Semester } from '../dto/Semester';

export type PlanWithSemesters = {
  code: string;
  decreeDate: string;
  id: number;
  inEffectSince: string;
  objectState: 'UNVERIFIED';
  studiesProgramId: number;
  semesters: Semester[];
};
