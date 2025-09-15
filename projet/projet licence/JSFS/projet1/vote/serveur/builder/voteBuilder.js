import Builder from "./Builder.js";
import { readFileSync } from 'fs';
import { join } from 'path';



const voteHtml = readFileSync(join(process.cwd(), 'public/vote.html'), 'utf-8');

export default class  AdminBuilder extends Builder {
    constructor(request, response, status, type) {
        super(request,response,status,type) ;
    
    }

    buildResponse() {
        this.response.writeHead(200, { 'Content-Type': 'text/html' });
        this.response.write(voteHtml);
        this.response.end();
    }
}