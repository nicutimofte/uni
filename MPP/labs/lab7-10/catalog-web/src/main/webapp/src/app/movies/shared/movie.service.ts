import {Injectable} from "@angular/core";
import {Http, Response, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {Movie} from "./movie.model";
/**
 * Created by Nicu on 4/11/17.
 */

@Injectable()
export class MovieService {
  private moviesUrl = 'http://localhost:8080/api/movies';
  private headers = new Headers({'Content-Type': 'application/json'});

  constructor(private http: Http) {
  }

  getMovies(): Observable<Movie[]> {
    return this.http.get(this.moviesUrl)
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body.movies || {};
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

  getMovie(id: number): Observable<Movie> {
    return this.getMovies().map(movies => movies.find(movie => movie.id === id));
  }

  update(movie): Observable<Movie> {
    const url = `${this.moviesUrl}/${movie.id}`;
    return this.http
      .put(url, JSON.stringify({"movie": movie}), {headers: this.headers})
      .map(this.extractData)
      .catch(this.handleError);
  }

  create(name:string , director:string, genre:string, availableCopies:number) : Observable<Movie>{
    let movie = {name, director, genre, availableCopies};
    return this.http
      .post(this.moviesUrl, JSON.stringify({"movie": movie}), {headers: this.headers})
      .map(this.extractData)
      .catch(this.handleError);
  }

  delete(id: number): Observable<void> {
    const url = `${this.moviesUrl}/${id}`;
    return this.http
      .delete(url, {headers: this.headers})
      .map(() => null)
      .catch(this.handleError);
  }

}
