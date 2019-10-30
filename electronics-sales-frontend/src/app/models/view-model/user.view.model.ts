import { Role } from './../types/role.type';
export interface User {
  id: number;

  username: string;

  firstname?: string;

  lastname?: string;

  phoneNumber?: string;

  email?: string;

  address?: string;

  gender?: boolean;

  birthday?: Date;

  avartarId?: number;

  role: Role;
}
