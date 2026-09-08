package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.Artesao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtesaoRepository extends JpaRepository<Artesao, Integer> {
}