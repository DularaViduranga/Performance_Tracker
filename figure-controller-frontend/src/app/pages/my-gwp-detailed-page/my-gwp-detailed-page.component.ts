import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MyFiguresService, MyGWPDetailedResponseDTO, ProductResponseDTO } from '../../services/myFigures.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { GenericTableComponent, TableConfig } from '../../core/components/shared/generic-table/generic-table.component';

@Component({
  selector: 'app-my-gwp-detailed-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatSelectModule,
    MatButtonModule,
    GenericTableComponent,
    MatIcon,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule  // Added for autocomplete
  ],
  templateUrl: './my-gwp-detailed-page.component.html'
})
export class MyGWPDetailedPageComponent implements OnInit {

  // GET FROM ROUTER
  intermediaryCode = '';
  start = '';
  end = '';

  // DROPDOWNS
  classCodes = ['ALL', 'MT', 'MS', 'MC', 'FI', 'EG', 'MD'];
  selectedClassCode = 'ALL';
  selectedProductCode = 'ALL';

  // Products
  products: ProductResponseDTO[] = [];
  filteredProducts: ProductResponseDTO[] = [];
  productSearchText: string | ProductResponseDTO = '';

  tableData: MyGWPDetailedResponseDTO[] = [];
  isLoading = false;

  tableConfig: TableConfig = {
    columns: [
    { key: 'policyNumber', header: 'Policy No', sortable: true },
    { key: 'productCode', header: 'Product', sortable: true },
    { key: 'customerName', header: 'Customer', sortable: true },
    { key: 'referenceNumber', header: 'Reference', sortable: true },
    { key: 'branchCode', header: 'Branch', sortable: true },
    { key: 'policyStartDate', header: 'Start', type: 'date', sortable: true },
    { key: 'policyEndDate', header: 'End', type: 'date', sortable: true },
    { key: 'transactionType', header: 'Transaction', sortable: true },
    { key: 'issuedDate', header: 'Issued Date', type: 'date', sortable: true },
    { key: 'sumInsured', header: 'Sum Insured', type: 'currency', align: 'right', sortable: true },
    { key: 'createdDate', header: 'Created Date', type: 'date', sortable: true },
    { key: 'productDescription', header: 'Product Description', sortable: true },
    { key: 'basic', header: 'Basic', type: 'currency', align: 'right', sortable: true },
    { key: 'srcc', header: 'SRCC', type: 'currency', align: 'right', sortable: true },
    { key: 'tc', header: 'TC', type: 'currency', align: 'right', sortable: true }
    ],
    showPagination: false,
    showFooter: false,
    showFilter: false,  // Show totals for these columns
    emptyMessage: 'No GWP data available',
    emptyIcon: 'work'};
  

  constructor(
    private route: ActivatedRoute,
    private service: MyFiguresService,
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.start = params['start'];
      this.end = params['end'];
      this.intermediaryCode = params['intermediaryCode'];
      
      console.log('Query Params:', { 
        start: this.start, 
        end: this.end, 
        intermediaryCode: this.intermediaryCode 
      });
    });
  }

  // When class changes
  onClassChange() {
    if (this.selectedClassCode === 'ALL') {
      this.selectedProductCode = 'ALL';
      this.products = [];
      this.filteredProducts = [];
      this.productSearchText = '';
      return;
    }

    this.service.getProductsByClass(this.selectedClassCode)
      .subscribe({
        next: (data) => {
          this.products = data;
          this.filteredProducts = [...data];
          this.productSearchText = '';
          console.log('Products loaded:', data);
        },
        error: (err) => {
          console.error('Error loading products:', err);
          this.products = [];
          this.filteredProducts = [];
        }
      });
  }

  // Filter products based on search text
  filterProducts() {
    const searchValue = typeof this.productSearchText === 'string' 
      ? this.productSearchText 
      : '';

    if (!searchValue.trim()) {
      this.filteredProducts = [...this.products];
      return;
    }

    const searchLower = searchValue.toLowerCase().trim();
    
    this.filteredProducts = this.products.filter(product => 
      product.prdCode.toLowerCase().includes(searchLower) ||
      product.prdDescription.toLowerCase().includes(searchLower)
    );
  }

  // Handle product selection from autocomplete
  onProductSelected(event: MatAutocompleteSelectedEvent) {
    const selected = event.option.value;
    
    if (selected === 'ALL') {
      this.selectedProductCode = 'ALL';
    } else if (typeof selected === 'object') {
      this.selectedProductCode = selected.prdCode;
    }
    
    console.log('Selected product code:', this.selectedProductCode);
  }

  // Display function for autocomplete
  displayProduct(product: ProductResponseDTO | string): string {
    if (product === 'ALL') {
      return 'ALL - All Products';
    }
    if (typeof product === 'object' && product) {
      return `${product.prdCode} - ${product.prdDescription}`;
    }
    return '';
  }

  // FETCH DETAILED DATA
  onSearch() {
    const cls = this.selectedClassCode ?? 'ALL';
    const prd = this.selectedProductCode ?? 'ALL';

    console.log('Searching with:', {
      intermediaryCode: this.intermediaryCode,
      start: this.start,
      end: this.end,
      productCode: prd,
      classCode: cls
    });

    this.isLoading = true;

    this.service.getMyGWPDetailed(
      this.intermediaryCode,
      this.start,
      this.end,
      prd,
      cls
    )
    .subscribe({
      next: (res) => {
        this.tableData = res;
        this.isLoading = false;
        console.log('Data loaded:', res);
      },
      error: (err) => {
        console.error('Error loading data:', err);
        this.tableData = [];
        this.isLoading = false;
      }
    });
  }
}