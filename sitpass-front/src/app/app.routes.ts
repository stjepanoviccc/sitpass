import { Routes } from '@angular/router';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { RegisterPageComponent } from './pages/register-page/register-page.component';
import { AccountsPageComponent } from './pages/dashboard/accounts-page/accounts-page.component';
import { ManagesPageComponent } from './pages/dashboard/manages-page/manages-page.component';
import { ProfilePageComponent } from './pages/profile-page/profile-page.component';
import { AdminGuard } from './services/auth/admin-guard.service';
import { UserOrAdminGuardService as UserOrAdminGuard } from './services/auth/user-or-admin-guard.service';
import { NoPermissionComponent } from './pages/error-pages/no-permission/no-permission.component';
import { NotFoundComponent } from './pages/error-pages/not-found/not-found.component';
import { FacilityPageComponent as ProfileFacilityPageComponent } from './pages/facility-page/facility-page.component';
import { FacilitiesPageComponent as AdminFacilitiesPageComponent } from './pages/dashboard/facilities-page/facilities-page.component';
import { FacilityEditPageComponent } from './pages/dashboard/facility-edit-page/facility-edit-page.component';
import { ReviewPageComponent } from './pages/review-page/review-page.component';
import { FacilityAnalyticsPageComponent } from './pages/dashboard/facility-analytics-page/facility-analytics-page.component';

export const routes: Routes = [
    { path: '', component: HomePageComponent, canActivate: [UserOrAdminGuard]},
    { path: 'login', component: LoginPageComponent},
    { path: 'register', component: RegisterPageComponent},
    { path: 'profile/:id', component: ProfilePageComponent, canActivate: [UserOrAdminGuard] },
    { path: 'facilities/:id', component: ProfileFacilityPageComponent, canActivate: [UserOrAdminGuard] },
    { path: 'reviews/:id', component: ReviewPageComponent, canActivate: [UserOrAdminGuard]},
    {
        path: 'dashboard',
        canActivate: [UserOrAdminGuard],
        children: [
            { path: 'accounts', component: AccountsPageComponent, canActivate: [AdminGuard] },
            { path: 'manages', component: ManagesPageComponent, canActivate: [AdminGuard] },
            { path: 'facilities', component: AdminFacilitiesPageComponent, canActivate: [UserOrAdminGuard] },
            { path: 'facilities/:id/edit', component: FacilityEditPageComponent, canActivate: [UserOrAdminGuard] },
            { path: 'facilities/:id/analytics', component: FacilityAnalyticsPageComponent, canActivate: [UserOrAdminGuard] }
        ]
    },
    { path: 'error/no-permission', component: NoPermissionComponent},
    { path: '**', component: NotFoundComponent}
];
    