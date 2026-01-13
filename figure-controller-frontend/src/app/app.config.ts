
import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';

import { ArrowDown, ArrowUp, ArrowUp01, Award, Briefcase, Building2, BuildingIcon, ChartBarIcon, ChevronRight, DownloadIcon, FilterIcon, Globe, icons, LogOut, LucideAngularModule, Menu, PinIcon, RefreshCcw, Settings, TrendingUp, UsersIcon, Wallet, XCircle } from 'lucide-angular';
import { Home, User, Search } from 'lucide-angular'; // ✅ FIXED IMPORT
import { authInterceptor } from './interceptor/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    // provideHttpClient(withInterceptors([authInterceptor])),


    // ✅ THIS is how NgModules are added in standalone
    importProvidersFrom(
      LucideAngularModule.pick({
        Home,
        User,
        Search,
        ArrowUp,
        ArrowDown,
        UsersIcon,
        ChevronRight,
        BuildingIcon,
        FilterIcon,
        DownloadIcon,
        ChartBarIcon,
        PinIcon,
        Settings,
        LogOut,
        TrendingUp,
        Wallet,
        RefreshCcw,
        XCircle,
        Award,
        Briefcase,
        Menu,
        Building2,
        Globe
      })
    )
  ]
};

