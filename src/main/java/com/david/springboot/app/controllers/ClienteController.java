package com.david.springboot.app.controllers;


import com.david.springboot.app.models.entity.Cliente;
import com.david.springboot.app.models.service.IClienteService;
import com.david.springboot.app.models.service.IUploadFileService;
import com.david.springboot.app.util.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String UPLOADS_FOLDER = "uploads";

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

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String Listar(@RequestParam(name="page", defaultValue = "0") int page, Model model){

        Pageable pageRequest = PageRequest.of(page, 5);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", " Listado de Clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model){
        Cliente cliente = new Cliente();


        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

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
}
