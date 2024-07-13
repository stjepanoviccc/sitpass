import { Component } from '@angular/core';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';
import { TableComponent } from '../../../components/shared/table/table.component';
import { AccountRequest } from '../../../models/AccountRequest';
import { RegisterService } from '../../../services/auth/register.service';
import { RequestStatus } from '../../../models/enums/RequestStatus';
import { ButtonComponent } from '../../../components/shared/button/button.component';
import { ModalComponent } from '../../../components/shared/modal/modal.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-accounts-page',
  standalone: true,
  imports: [WrapComponent, TableComponent, ModalComponent, ButtonComponent, FormsModule],
  templateUrl: './accounts-page.component.html',
})
export class AccountsPageComponent {
  accountRequestsData: AccountRequest[] = [];
  selectedRowData: any = null; 
  rejectionReason: string = '';
  showModal: boolean = false;

  constructor(private registerService: RegisterService) { }

  ngOnInit(): void {
    this.loadAccountRequestsData();
  }

  loadAccountRequestsData(): void {
    this.registerService.findAll().subscribe( (data: AccountRequest[]) => { this.accountRequestsData = data; }
    );
  }

  acceptRequest(rowData: AccountRequest): void {
    this.registerService.handleAccountRequests(rowData, RequestStatus.ACCEPTED).subscribe(data =>{
      this.accountRequestsData = this.accountRequestsData.filter(acc => acc.id !== data.id);
    });
  }

  rejectRequest(rowData: AccountRequest): void {
    this.selectedRowData = rowData;
    this.toggleRejectionReasonModal()
  }

  confirmRejection(): void {
    if(this.rejectionReason != "") {
      this.selectedRowData.rejectionReason = this.rejectionReason;
    }
    this.registerService.handleAccountRequests(this.selectedRowData, RequestStatus.REJECTED).subscribe(data =>{
      this.accountRequestsData = this.accountRequestsData.filter(acc => acc.id !== data.id);
    });
    this.rejectionReason = "";
    this.toggleRejectionReasonModal();
  }

  toggleRejectionReasonModal(): void {
    this.showModal = !this.showModal;
    this.rejectionReason = "";
  }
}
