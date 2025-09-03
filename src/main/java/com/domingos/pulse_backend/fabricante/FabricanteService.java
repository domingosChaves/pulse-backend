package com.domingos.pulse_backend.fabricante;

import com.domingos.pulse_backend.fabricante.port.FabricantePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FabricanteService {

    private final FabricantePort port;

    public FabricanteService(FabricantePort port) {
        this.port = port;
    }

    public Fabricante criar(FabricanteDTO dto) {
        // checar cnpj duplicado
        port.findByCnpj(dto.getCnpj()).ifPresent(f -> {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        });
        Fabricante f = new Fabricante(dto.getNome(), dto.getCnpj(), dto.getEndereco(), dto.getTelefone(), dto.getContato());
        return port.save(f);
    }

    public List<Fabricante> listar() {
        return port.findAll();
    }

    public Fabricante buscarPorId(Long id) {
        return port.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com id: " + id));
    }

    public Fabricante atualizar(Long id, FabricanteDTO dto) {
        Fabricante existente = buscarPorId(id);
        // se cnpj alterado, verificar duplicidade
        if (!existente.getCnpj().equals(dto.getCnpj())) {
            port.findByCnpj(dto.getCnpj()).ifPresent(f -> {
                throw new IllegalArgumentException("CNPJ já cadastrado");
            });
            existente.setCnpj(dto.getCnpj());
        }
        existente.setNome(dto.getNome());
        existente.setEndereco(dto.getEndereco());
        existente.setTelefone(dto.getTelefone());
        existente.setContato(dto.getContato());
        return port.save(existente);
    }

    public void excluir(Long id) {
        Fabricante existente = buscarPorId(id);
        port.delete(existente);
    }
}
