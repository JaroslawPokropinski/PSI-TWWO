export type Plan = {
  code: string;
  decreeDate: string;
  id: number;
  inEffectSince: string;
  objectState?: 'UNVERIFIED' | 'VERIFIED';
  studiesProgramId: number;
};
