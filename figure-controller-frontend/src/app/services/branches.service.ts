import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs/internal/Observable";
import { Injectable } from "@angular/core";

export interface BranchAccumulatedPerformanceDTO {
  branchCode: string;
  branchName: string;
  currentMonthGwp: number;
  accumulatedGwp: number;
}

export interface GetAllBranchesFromDaily {
  branchCode: string;
  branchName: string;
}

export interface MonthWiseRegionGwpDTO {
  month: number;
  monthlyGwp: number;
}


@Injectable({
  providedIn: 'root'
})

export class BranchesService {
  private API_Url = environment.apiUrl+'api/v1/branches';

  constructor(private http: HttpClient) {}

  getBranchAccumulatedPerformance(branchCode: string) : Observable<BranchAccumulatedPerformanceDTO> {
    let params = new HttpParams().set('branchCode', branchCode);
    return this.http.get<BranchAccumulatedPerformanceDTO>(`${this.API_Url}/branchAccumulatedPerformance`, { params });
  }


  getTop10AccumulatedBranchesFromDaily(): Observable<BranchAccumulatedPerformanceDTO[]> {
    return this.http.get<BranchAccumulatedPerformanceDTO[]>(`${this.API_Url}/getTop10AccumulatedBranchesFromDaily`);
  }

  getAllBranchesFromDaily(): Observable<GetAllBranchesFromDaily[]> {
    return this.http.get<GetAllBranchesFromDaily[]>(`${this.API_Url}/getAllBranchesFromDaily`);
  }

  getMonthWiseBranchGwp(branchCode: string, year: number) : Observable<MonthWiseRegionGwpDTO[]> {
    let params = new HttpParams().set('branchCode', branchCode).set('year', year);
    return this.http.get<MonthWiseRegionGwpDTO[]>(`${this.API_Url}/getMonthWiseBranchGwp`, { params });
  }

}
