import { Component } from '@angular/core';
import {User, UserResponse, UserRoleService} from './user-role.service';
import {MatCheckboxChange, MatDialog, MatSelectChange} from '@angular/material';
import {HttpErrorResponse} from '@angular/common/http';
import {DialogComponent} from './dialog/dialog.component';

const ADMIN_ROLE = 'ADMIN';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  data: Array<UserResponse> = [];

  private ongoing = false;

  constructor(public userService: UserRoleService, public dialog: MatDialog) {
    this.userService.getUsers().subscribe(user => this.data = user);
  }

  nonAdminRole(user: User) {
    return user.roles.find(r => r !== ADMIN_ROLE);
  }

  isAdmin(user: User) {
    return user.roles.indexOf(ADMIN_ROLE) >= 0;
  }

  adminChanged($event: MatCheckboxChange, user: UserResponse) {
    const old = user.roles;
    if ($event.checked) {
      user.roles.push(ADMIN_ROLE);
    } else {
      user.roles = user.roles.filter(r => r !== ADMIN_ROLE);
    }
    this.updateRoles(user, old);
  }

  roleSelected($event: MatSelectChange, user: UserResponse) {
    const old = user.roles;
    const roles = this.isAdmin(user) ? [ADMIN_ROLE] : [];
    const value = $event.value;
    roles.push(...(!!value ? [value] : []));
    user.roles = roles;
    this.updateRoles(user, old);
  }

  private updateRoles(user: UserResponse, old: string[]) {
    if (!this.ongoing) {
      this.ongoing = true;
      this.userService.saveRoles(user).subscribe({
        error: (err: HttpErrorResponse) => {
          this.dialog.open(DialogComponent, {
            data: {
              title: 'Error!',
              msg: 'Can not update User: ' + err.error
            }
          });
          user.roles = old;
          this.ongoing = false;
        },
        complete: () => this.ongoing = false
      });
    }
  }

  create(pseudo: HTMLInputElement) {
    const user: User = {pseudonym: pseudo.value, roles: ['CONTRACT_READ']};
    this.userService.createUser(user).subscribe({
      next: ur => {
        this.dialog.open(DialogComponent, {
          data: {
            title: 'Success!',
            msg: 'Created User.'
          }
        });
        pseudo.value = '';
        this.data.push(ur);
      },
      error: (err: HttpErrorResponse) => {
        this.dialog.open(DialogComponent, {
          data: {
            title: 'Error!',
            msg: 'Can not create User: ' + err.error
          }
        });
        pseudo.value = '';
      }
    });
  }
}
