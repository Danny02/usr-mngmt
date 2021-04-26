import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatCheckboxModule, MatDialogModule,
  MatExpansionModule,
  MatFormFieldModule,
  MatInputModule,
  MatRadioModule,
  MatSelectModule
} from '@angular/material';
import { DialogComponent } from './dialog/dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    DialogComponent
  ],
  entryComponents: [
    DialogComponent
  ],
  imports: [
    BrowserAnimationsModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatRadioModule,
    MatSelectModule,
    MatCheckboxModule,
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    MatDialogModule,
    MatButtonModule,
    MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
