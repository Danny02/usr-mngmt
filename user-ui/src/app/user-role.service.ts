import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {delay, flatMap, map} from 'rxjs/operators';
import * as traverson from 'traverson';
import * as JsonHalAdapter from 'traverson-hal';

export interface User {
  pseudonym: string;
  roles: Array<string>;
}

export interface UserLinks {
  _links: {
    roles: {
      href: string
    }
  };
}

export type UserResponse = User & UserLinks;

interface InAction {
  abort(): void;
}

interface Builder {
  getResource(callback: (err: any, document: any) => void): InAction;
}

@Injectable({
  providedIn: 'root'
})
export class UserRoleService {

  constructor(private readonly client: HttpClient) {
    traverson.registerMediaType(JsonHalAdapter.mediaType, JsonHalAdapter);
  }

  getUsers(): Observable<Array<UserResponse>> {
    const builder = traverson.from('http://localhost:8080').jsonHal().follow('users', 'item[$all]');
    return this.toObservable(builder).pipe(
      map(resp => resp as Array<UserResponse>)
    );
  }

  saveRoles(user: UserResponse) {
    return this.client.put('http://localhost:8080' + user._links.roles.href, user.roles);
  }

  createUser(user: User): Observable<UserResponse> {
    const builder = traverson.from('http://localhost:8080').jsonHal();
    return this.toObservable(builder).pipe(
      flatMap(resp => this.client.post('http://localhost:8080' + resp._links.users.href, user, {observe: 'body'})),
      map(body => body as UserResponse)
    );
  }

  private toObservable(builder: Builder): Observable<any> {
    return new Observable(subscriber => {
        const action = builder.getResource((err, doc) => {
          if (err) {
            subscriber.error(err);
          } else {
            subscriber.next(doc);
          }
        });

        return () => action.abort();
    });
  }
}
