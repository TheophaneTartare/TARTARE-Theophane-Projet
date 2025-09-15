import { readFileSync } from 'fs';
import { join } from 'path';
import { URL } from 'url';
import Builder from "../builder/Builder.js"
import aboutBuilder from "../builder/aboutBuilder.js"
import voteBuilder from "../builder/voteBuilder.js"
import adminBuilder from "../builder/adminBuilder.js"

export default class RequestController {
  #request;
  #response;
  #url;

  constructor(request, response) {
    this.#request = request;
    this.#response = response;
    this.#url = new URL(request.url, `http://${request.headers.host}`);
  }

  get response() {
    return this.#response;
  }

  handleRequest() {
    this.buildResponse();
  }

  buildResponse() {
    const nameValue = this.#url.searchParams.get('name') || 'unknown';
    let filePath ;
  
    if (this.#url.pathname === '/about') {
      new aboutBuilder(this.#request, this.#response, 200, "about").handleRequest();
    } else if (this.#url.pathname === '/admin-vote') {
      new adminBuilder(this.#request, this.#response, 200, "admin").handleRequest();
    } else if (this.#url.pathname === '/votant') {
      new voteBuilder(this.#request, this.#response, 200, "vote").handleRequest();
    } else if (this.#url.pathname === '/') {
      new Builder(this.#request, this.#response, 200, "home").handleRequest();
    } else if (this.#url.pathname.startsWith('/scripts/')  ){
      const name = this.#url.pathname.split('/').pop() ;
      filePath =`./public/scripts/${name}`;
      let contentType = 'application/javascript'; 
      this.#response.writeHead(200 , {'Content-Type': contentType}) ;
      this.#response.write(readFileSync(filePath,'utf-8')) ;
    } else if   (this.#url.pathname.startsWith('/style/')  ){
      const name = this.#url.pathname.split('/').pop() ;
      filePath =`./public/style/${name}`;
      let contentType = 'text/css'; 
      this.#response.writeHead(200 , {'Content-Type': contentType}) ;
      this.#response.write(readFileSync(filePath,'utf-8')) ;
     } else {
      this.#response.writeHead(404, { 'Content-Type': 'text/plain' });
      this.#response.write("Erreur: Page non trouvée");
      this.#response.end();
    }
    
    
    
    this.#response.end();
  }
}