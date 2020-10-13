package net.tecgurus.minipos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.tecgurus.minipos.exception.ProductoNoEncontradoException;
import net.tecgurus.minipos.model.Producto;
import net.tecgurus.minipos.repository.ProductoRepository;

@Service
public class ProductoService {

	@Autowired
	private ProductoRepository productoRepository;
	
	@Transactional(readOnly = true)
	public List<Producto> listar() {
		return productoRepository.findAll();
	}
	
	@Transactional
	public Producto guardar(Producto producto) {   // Poco acoplamiento 
		return productoRepository.save(producto);
	}
	
	@Transactional
	public boolean eliminar(int idProducto) {
		Optional<Producto> optProducto = productoRepository.findById(idProducto);//SELECT * FROM Producto WHERE idProducto = 1
		if (optProducto.isPresent()) {
			productoRepository.deleteById(idProducto); //DELETE FROM Producto WHERE idCategoria = 1
			return true;
		} else {
			throw new ProductoNoEncontradoException("Producto no existe: " + idProducto);
		}
	}

	@Transactional(readOnly = true)
	public List<Producto> buscar(String filtro) {
		// TODO Auto-generated method stub
		return productoRepository.findByNombreOrDescripcionOrSku(filtro);
	}
	
}
