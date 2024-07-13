import { Component, HostListener } from '@angular/core';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { WrapComponent } from '../shared/wrap/wrap.component';
import { SocialsComponent } from '../shared/socials/socials.component';
import { ButtonComponent } from '../shared/button/button.component';
import { ModalComponent } from '../shared/modal/modal.component';
import { LoginService } from '../../services/auth/login.service';
import { UserService } from '../../services/user/user.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-header',
    standalone: true,
    templateUrl: './header.component.html',
    imports: [WrapComponent, RouterLink, SocialsComponent, ButtonComponent, ModalComponent, CommonModule]
})
export class HeaderComponent {
  emailFromToken: string | null = localStorage.getItem('email')
  isLargeScreen = window.innerWidth >= 1280;
  showMenu = false;
  userId: number | undefined;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isLargeScreen = event.target.innerWidth >= 1280;
    this.showMenu = false;
  }

  ngOnInit() {
    this.userService.findByEmail(this.emailFromToken!).subscribe(data => {
      this.userId = data.id;
    });
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (this.isLargeScreen) {
          return;
        }
        this.showMenu = false;
      }
    });
  }

  constructor (public loginService: LoginService, private userService: UserService, private router: Router) {} 

  toggleMenu() {
    this.showMenu = !this.showMenu;
  }

}
