import { Component, OnInit } from '@angular/core';
import {Client} from "../shared/client.model";
import {Router} from "@angular/router";
import {ClientService} from "../shared/client.service";

@Component({
  moduleId: module.id,
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {
  errorMessage: string;
  clients: Client[];
  selectedClient: Client;

  constructor(private clientService: ClientService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getClients();
  }

  getClients() {
    this.clientService.getClients()
      .subscribe(
        clients => this.clients = clients,
        error => this.errorMessage = <any>error
      );
  }

  onSelect(client: Client): void {
    this.selectedClient = client    ;
  }

  gotoDetail(): void {
    this.router.navigate(['/client/detail/', this.selectedClient.id]);
  }

  delete(client: Client): void {
    this.clientService.delete(client.id)
      .subscribe(() => {
        this.clients = this.clients.filter(c => c !== client);
        if (this.selectedClient === client) {
          this.selectedClient = null;
        }
      });
  }

}
