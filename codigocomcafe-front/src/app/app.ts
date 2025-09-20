import { Component, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Router } from "./component/router/router";


@Component({
  selector: 'app-root',
  imports: [Router],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('codigocomcafe-front');
}
