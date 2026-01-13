import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface MyGWPResponseDTO {
  policyNumber: string;
  basic: number;
  srcc: number;
  tc: number;
}

export interface MyGWPDetailedResponseDTO {
  policyNumber: string;
  productCode: string;
  customerName: string;
  referenceNumber: string;
  branchCode: string;
  policyStartDate: string;
  policyEndDate: string;
  transactionType: string;
  issuedDate: string;
  sumInsured: number;
  createdDate: string;
  productDescription: string;
  basic: number;
  srcc: number;
  tc: number;
}

export interface ProductResponseDTO {
  prdCode: string;
  prdDescription: string;
}

export interface MyPerformanceResponseDTO {
  classCode: string;
  target: number;
  achieved: number;
  percentage: number;
}

export interface MyCashCollectionDTO {
  branchName: string;
  cusName: string;
  tranType: string;
  settledAmount: number;
  debPolicyNo: string;
  debClaCode: string;
  proDesc: string;
}

export interface MyCancellationResponseDTO {
  policyNo: string;
  classDesc: string;
  prodDesc: string;
  customerName: number;
  phoneNumber: string;
  createdDate: string;
  periodFrom: string;
  periodTo: string;
  branchName: number;
}

export interface MyRenewalResponseDTO {
  policyNo: string;
  phone: string;
  classDesc: string;
  prodDesc: string;
  customerName: string;
  createdDate: string;
  periodFrom: string;
  periodTo: string;
  branchName: number;
}

export interface MyNewBuisnessDTO {
  policyNo: string;
  classDesc: string;
  prodDesc: string;
  customerName: string;
  phoneNumber: string;
  createdDate: string;
  periodFrom: string;
  periodTo: string;
  branchName: number;
  status: string;
  transactionDesc: string;
}

export interface MyNewBuisnessCountsByTransactionTypesDTO {
  type: string;
  count: number;
}

@Injectable({
  providedIn: 'root'
})
export class MyFiguresService {
  private API_Url = environment.apiUrl+'api/v1/myFigures';

  constructor(private http: HttpClient) {}

  getMyGWP(intermediaryCode: string, start: string, end: string): Observable<MyGWPResponseDTO[]> {
    let params = new HttpParams()
      .set('intermediaryCode', intermediaryCode)
      .set('start', start)
      .set('end', end);

    return this.http.get<MyGWPResponseDTO[]>(`${this.API_Url}/myGWP`, { params });
  }

  getProductsByClass(classCode: string) : Observable<ProductResponseDTO[]>{
    let params = new HttpParams()
      .set('classCode', classCode);

    return this.http.get<ProductResponseDTO[]>(`${environment.apiUrl}api/v1/getProducts/productByClass`, { params });
  }

  getMyGWPDetailed(intermediaryCode: string, start: string, end: string, productCode: string, cla_code: string): Observable<MyGWPDetailedResponseDTO[]> {
    let params = new HttpParams()
      .set('intermediaryCode', intermediaryCode)
      .set('start', start)
      .set('end', end)
      .set('productCode', productCode)
      .set('classCode', cla_code);
    return this.http.get<MyGWPDetailedResponseDTO[]>(`${this.API_Url}/myGWPDetailed`, { params });
  }

  getMyPerformance(intermediaryCode: string, year: string, month: string): Observable<MyPerformanceResponseDTO[]> {
    let params = new HttpParams()
      .set('intermediaryCode', intermediaryCode)
      .set('year', year)
      .set('month', month);

    return this.http.get<MyPerformanceResponseDTO[]>(`${this.API_Url}/performanceByClass`, { params });
  }

  getPerformanceByPeriod(intermediaryCode: string, start: string, end: string): Observable<MyPerformanceResponseDTO[]> {
    let params = new HttpParams()
      .set('intermediaryCode', intermediaryCode)
      .set('start', start)
      .set('end', end);

    return this.http.get<MyPerformanceResponseDTO[]>(`${this.API_Url}/performanceByClassPeriod`, { params });
  }

  getMyCashCollection(intermediaryCode: string, start: string, end: string): Observable<MyCashCollectionDTO[]> {
    let params = new HttpParams()
      .set('intermediaryCode', intermediaryCode)
      .set('start', start)
      .set('end', end);

    return this.http.get<MyCashCollectionDTO[]>(`${this.API_Url}/myCashCollection`, { params });
  }

  getMyCancellation(start: string, end: string,intermediaryCode: string, ): Observable<MyCancellationResponseDTO[]> {
    let params = new HttpParams()
      .set('start', start)
      .set('end', end)
      .set('intermediaryCode', intermediaryCode);

    return this.http.get<MyCancellationResponseDTO[]>(`${this.API_Url}/myCancellation`, { params });
  }

  getMyRenewal(start: string, end: string,intermediaryCode: string, ): Observable<MyRenewalResponseDTO[]> {
    let params = new HttpParams()
      .set('start', start)
      .set('end', end)
      .set('intermediaryCode', intermediaryCode);

    return this.http.get<MyRenewalResponseDTO[]>(`${this.API_Url}/myRenewal`, { params });
  }

  getMyNewBuisness(start: string, end: string,intermediaryCode: string, ): Observable<MyNewBuisnessDTO[]> {
    let params = new HttpParams()
      .set('start', start)
      .set('end', end)
      .set('intermediaryCode', intermediaryCode);

    return this.http.get<MyNewBuisnessDTO[]>(`${this.API_Url}/myNewBusiness`, { params });
  }

  getMyNewBuisnessCountsByTransactionTypes(start: string, end: string,intermediaryCode: string, ): Observable<MyNewBuisnessCountsByTransactionTypesDTO[]> {
    let params = new HttpParams()
      .set('start', start)
      .set('end', end)
      .set('intermediaryCode', intermediaryCode);

    return this.http.get<MyNewBuisnessCountsByTransactionTypesDTO[]>(`${this.API_Url}/myNewBusinessCountByTranType`, { params });
  }

}
