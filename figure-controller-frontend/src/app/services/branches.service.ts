import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs/internal/Observable";

export interface BranchAccumulatedPerformanceDTO {
  id: number;
  branchCode: string;
  currentMonthGwp: number;
  accumulatedGwp: number;
  snapshotDate: Date;
}

export class BranchesService {
  private API_Url = environment.apiUrl+'api/v1/branches';

  constructor(private http: HttpClient) {}

  getBranchAccumulatedPerformance(branchCode: string) : Observable<BranchAccumulatedPerformanceDTO> {
    let params = new HttpParams().set('branchCode', branchCode);
    return this.http.get<BranchAccumulatedPerformanceDTO>(`${this.API_Url}/branchAccumulatedPerformance`, { params });
  }

}

