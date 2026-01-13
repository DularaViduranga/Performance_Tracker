import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface SFCCodeResponseDTO {
  sfcCode: string;
  sfcName: string;
  brnName: string;
}

export interface UserResponseDTO {
  intermediaryCode: string;
  name: string;
}

export interface SalesOfficersByBranchResponseDTO {
  sfcCode: string;
  sfcName: string;
}

export interface LoginResponseDTO {
  username: string;
  epf: string;
  level: string;
  locationName: string;
  company: string;
  dpt: string;
  empType: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_Url = environment.apiUrl + 'api/v1/users';
  private platformId = inject(PLATFORM_ID);
  private isBrowser: boolean;


  constructor(private http: HttpClient) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  getSFC(sfcCode: string, fromDate: string, toDate: string): Observable<SFCCodeResponseDTO[]> {
    let params = new HttpParams()
      .set('sfcCode', sfcCode)
      .set('fromDate', fromDate)
      .set('toDate', toDate);

    return this.http.get<SFCCodeResponseDTO[]>(`${this.API_Url}/getSFCCodesBySFC`, { params });
  }

  getSFCCodesByBranch(branchCode: string, fromDate: string, toDate: string): Observable<SalesOfficersByBranchResponseDTO[]> {
    let params = new HttpParams()
      .set('branchCode', branchCode)
      .set('fromDate', fromDate)
      .set('toDate', toDate);

    return this.http.get<SalesOfficersByBranchResponseDTO[]>(`${this.API_Url}/getSFCCodesByBranch`, { params });
  }

}

