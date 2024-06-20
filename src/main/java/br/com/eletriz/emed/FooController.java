package br.com.eletriz.emed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/foo")
public class FooController {

	@GetMapping("empty")
	public Flux<?> empty(){
		return ret(Flux.empty());
	}
	@GetMapping("ok")
	public Flux<?> ok(){
		Flux<String> v = Flux.just("A", "B", "C");
		return ret(v);
	}

	@GetMapping("error")
	public Flux<?> err(){
		return ret(Flux.error(new RuntimeException()));
	}
	
	public Flux<?> ret(Flux<?> v){
		return v.hasElements().flatMapMany(h -> h ? v : Flux.just(ResponseEntity.noContent().build()))
				.onErrorResume(err -> Flux.just(ResponseEntity.notFound().build()));
	}
}
