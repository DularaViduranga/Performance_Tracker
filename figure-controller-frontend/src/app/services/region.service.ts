import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs/internal/Observable";

export interface BranchesByRegionDTO {
  brnCode: string;
  brnName: string;
  regionName: string;
  currentMonthGwp : number;
  accumulatedGwp : number;
}

export interface RegionsWithGWPDTO {
  regionCode: string;
  regionName: string;
  currentMonthGwp: number;
  accumulatedGwp: number;
}

@Injectable({
  providedIn: 'root'
})
export class RegionService {
  private API_Url = environment.apiUrl+'api/v1/regions';

  constructor(private http: HttpClient) {}

  getBranchesByRegion(regionCode: string) : Observable<BranchesByRegionDTO[]> {
    let params = new HttpParams().set('regionCode', regionCode);
    return this.http.get<BranchesByRegionDTO[]>(`${this.API_Url}/getBranchesByRegion`, { params });
  }

  getRegionsWithGWP() : Observable<RegionsWithGWPDTO[]> {
    return this.http.get<RegionsWithGWPDTO[]>(`${this.API_Url}/getRegionsWithGWP`);
  }

}

