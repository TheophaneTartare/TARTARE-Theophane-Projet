import Builder from "./Builder.js";
import { readFileSync } from 'fs';
import { join } from 'path';


const adminHtml = readFileSync('public/admin.html', 'utf-8');


export default class  AdminBuilder extends Builder {
    constructor(request, response, status, type) {
        super(request,response,status,type) ;
    
    }

    buildResponse() {
        this.response.writeHead(200, { 'Content-Type': 'text/html' });
        this.response.write(adminHtml);
        this.response.end();
    }
}