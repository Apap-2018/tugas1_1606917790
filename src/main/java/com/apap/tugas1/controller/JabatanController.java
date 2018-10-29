package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String addJabatan(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		model.addAttribute("title", "Tambah Jabatan");
		return "add-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatan(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.add(jabatan);
		model.addAttribute("msg", "Data jabatan berhasil dimasukkan.");
		model.addAttribute("title", "Sukses");
		return "message";
	}
	
	@RequestMapping(value = "/jabatan/view", method = RequestMethod.GET)
	private String viewJabatan(@RequestParam(value = "idJabatan") long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(idJabatan);
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("jumlahKaryawan", jabatan.getListPegawai().size());
		model.addAttribute("title", "Detail Jabatan");
		return "detail-jabatan";
	}
	@RequestMapping(value = "/jabatan/viewall", method = RequestMethod.GET)
	private String viewAllJabatan(Model model) {
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		model.addAttribute("title", "Lihat Semua Jabatan");
		return "view-all-jabatan";
	}
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String updateJabatan(@RequestParam(value = "idJabatan") long idJabatan, Model model) {
		JabatanModel jabatan = jabatanService.getJabatanDetailById(idJabatan);
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("title", "Ubah Jabatan");
		return "update-Jabatan";
	}
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String updateJabatan(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.add(jabatan);
		model.addAttribute("msg", "Data jabatan telah diubah.");
		model.addAttribute("title", "Sukses");
		return "message";
	}
	
	@RequestMapping(value = "/jabatan/hapus", method = RequestMethod.POST)
	private String deleteJabatan(@ModelAttribute JabatanModel jabatan, Model model) {
		JabatanModel jabatanTemp = jabatanService.getJabatanDetailById(jabatan.getId());
		
		if(jabatanTemp.getListPegawai().isEmpty()) {
			jabatanService.deleteJabatanById(jabatanTemp.getId());
			model.addAttribute("msg", "Data jabatan telah dihapus.");
			model.addAttribute("title", "Sukses");
		}
		else {
			model.addAttribute("msg", "Data jabatan gagal diubah.");
			model.addAttribute("title", "Gagal");
		}
		return "message";
	}
}
