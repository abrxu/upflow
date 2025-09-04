export enum DepartmentRole {
  MEMBER = 'MEMBER',
  MANAGER = 'MANAGER',
  LEAD = 'LEAD',
  CONTRIBUTOR = 'CONTRIBUTOR'
}

export interface UserAssociation {
  departmentId: number;
  role: DepartmentRole;
}

export interface UserCreationPayload {
  username: string;
  name: string;
  lastName: string;
  email: string;
  password: string;
  associations: UserAssociation[];
}