package net.tecgurus.minipos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.tecgurus.minipos.dto.InventarioDTO;
import net.tecgurus.minipos.model.Compra;
import net.tecgurus.minipos.model.Compraproducto;
import net.tecgurus.minipos.model.Producto;
import net.tecgurus.minipos.repository.CompraProductoRepository;
import net.tecgurus.minipos.repository.CompraRepository;
import net.tecgurus.minipos.repository.InventarioRepository;
import net.tecgurus.minipos.repository.ProductoRepository;

@Service
public class CompraService {

	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private CompraProductoRepository compraProductoRepository;
	
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private InventarioRepository inventarioRepository;
	
	
	
	// Validar que al comprar los productos se garantice que haya 10 de remanente (Punto de reorden)
	// si no hay ese remanente entonces regresar una excepción StockNoDisponibleException(CONFLICT) 
	@Transactional
	public Compra guardar(Compra compra) {
		//Validar que la suma de los costos sea igual al total de la compra.
		
		//Validarme si el cliente no tiene adeudos, no le puedo vender mas productos (ClienteService , EstadoCuentaService)
		
		// UML Lenguaje de modelado unificado ... Diagramas clases, secuencias, interaccion, casos de uso.
		
		Compra compraActualizada = compraRepository.save(compra); //idcompra 23,24,25.....9999
		
		for(Compraproducto compraProducto : compra.getCompraproductos()) {
			compraProducto.setCompra(compraActualizada);
			Producto producto = productoRepository.findById(compraProducto.getProducto().getIdproducto()).get();
			
			int stock = producto.getStock();
			
			producto.setStock(stock - compraProducto.getCantidad());
			
			productoRepository.save(producto);  //UPDATE
			
			compraProductoRepository.save(compraProducto);  //INSERT
		}
		
		
		
		return compraActualizada;
	}
	
	
	@Transactional  //No puede ser solo lectura en caso de stored procedures 
	public List<InventarioDTO> consultarInventario() {
		return inventarioRepository.consultarInventario();
	}
	
	
	
	@Transactional(readOnly = true)
	public List<Compra> listar() {
		return compraRepository.findAll();
	}
	
	
}
