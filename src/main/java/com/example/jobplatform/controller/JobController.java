package com.example.jobplatform.controller;
import com.example.jobplatform.dto.JobDtos.*;import com.example.jobplatform.service.JobService;import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/jobs") public class JobController {private final JobService jobs;public JobController(JobService j){jobs=j;}
 @PostMapping public ResponseEntity<JobResponse> submit(@Valid @RequestBody SubmitJobRequest r,java.security.Principal p){return ResponseEntity.accepted().body(jobs.submit(p.getName(),r));}
 @GetMapping("/{id}") public JobResponse get(@PathVariable UUID id,java.security.Principal p){return jobs.get(p.getName(),id);}
 @GetMapping public List<JobResponse> list(java.security.Principal p){return jobs.list(p.getName());}
 @DeleteMapping("/{id}") public JobResponse cancel(@PathVariable UUID id,java.security.Principal p){return jobs.cancel(p.getName(),id);}
}
