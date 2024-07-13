import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ScrollService } from './services/router/scroll.service';

@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    imports: [RouterOutlet, CommonModule, RouterLink, HeaderComponent, FooterComponent]
})
export class AppComponent {
  title: string = 'sitpass-frontend';

  constructor(private router: Router, private scrollService: ScrollService) {}

  ngOnInit(): void {
    this.scrollService.subscribeToRouteChanges();
  }

  isLoginPage(): boolean { return this.router.url == '/login' }
  isRegisterPage(): boolean { return this.router.url == '/register'}
  isErrorPage(): boolean { return this.router.url.startsWith('/error'); }
}
