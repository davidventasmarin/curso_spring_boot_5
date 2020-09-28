package com.david.springboot.app.controllers;


import com.david.springboot.app.models.entity.Cliente;
import com.david.springboot.app.models.service.IClienteService;
import com.david.springboot.app.models.service.IUploadFileService;
import com.david.springboot.app.util.paginator.PageRender;
import com.david.springboot.app.view.xml.ClienteList;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

    @Autowired
    private MessageSource messageSource;

    @Secured("ROLE_USER")
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verfoto(@PathVariable String filename){

        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + recurso.getFilename() + "\"").
                body(recurso);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Cliente cliente = clienteService.findOne(id);
        if(cliente == null){
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return  "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Detalle Cliente: " + cliente.getNombre());

        return "ver";
    }

    @GetMapping(value = "/listar-rest")
    public @ResponseBody ClienteList listarRest(){

        return new ClienteList(clienteService.findAll());
    }

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String Listar(@RequestParam(name="page", defaultValue = "0") int page, Model model,
                         Authentication authentication, HttpServletRequest request, Locale locale) {

        if(authentication != null){
            logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            logger.info("Utilizando la forma estática SecurityContexFolder.getContext().getAuthentication()");
        }

        if(hasRole("ROLE_ADMIN")){
            logger.info("Hola ".concat(auth.getName()).concat(" Tienes acceso"));
        }else{
            logger.info("Hola ".concat(auth.getName()).concat(" No tienes acceso"));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

        if(securityContext.isUserInRole("ROLE_ADMIN")){
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola '".concat(auth.getName()).concat("' Tienes acceso"));
        }else{
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola '".concat(auth.getName()).concat("' No tienes acceso"));
        }

        if(request.isUserInRole("ROLE_ADMIN")){
            logger.info("Forma usando HttpServletRequest: Hola '".concat(auth.getName()).concat("' Tienes acceso"));
        }else{
            logger.info("Forma usando HttpServletRequest: Hola '".concat(auth.getName()).concat("' No tienes acceso"));
        }

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model){
        Cliente cliente = new Cliente();


        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Cliente cliente = null;

        if(id > 0){
            cliente = clienteService.findOne(id);
            if(cliente == null){
                flash.addFlashAttribute("error", "El Cliente no existe");
                return "redirect:/listar";
            }
        }
        else{
            flash.addFlashAttribute("error", "El ID del Cliente no puede ser cero");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
                          @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status){

        if(result.hasErrors()){
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        if(!foto.isEmpty()){

            if(cliente.getId() != null &&
                    cliente.getId() > 0 &&
                    cliente.getFoto() != null && cliente.getFoto().length() > 0){
               uploadFileService.delete(cliente.getFoto());
            }

            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename + "'");

            cliente.setFoto(uniqueFilename);
        }

        String mensajeFlash;

        if(cliente.getId() != null){
            mensajeFlash = "Cliente editado con éxito";
        } else {
            mensajeFlash = "Cliente creado con éxito";
        }

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value="/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){
        if(id > 0){
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente Eliminado con éxito.");

            if(uploadFileService.delete(cliente.getFoto())){
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito!");
            }
        }

        return "redirect:/listar";
    }

    private boolean hasRole(String role){
        SecurityContext context = SecurityContextHolder.getContext();

        if(context != null){
            return false;
        }
        Authentication auth = context.getAuthentication();
        if(auth != null){
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(role));
        /**
        for(GrantedAuthority authority: authorities){
            if(role.equals(authority.getAuthority())){
                return true;
            }
        }

        return false;
         **/
    }
}
