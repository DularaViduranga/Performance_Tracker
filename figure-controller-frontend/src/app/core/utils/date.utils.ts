export class DateUtils {
  // ✅ Fixed: Avoid timezone shift
  static toIso(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`; // "2025-12-01"
  }

  static toGwp(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    return `${day} ${months[date.getMonth()]} ${date.getFullYear()}`;
  }

  // Safe: YYYY-MM-DD string → Date object (timezone-safe)
  static parseIso(isoString: string): Date {
    const [y, m, d] = isoString.split('-').map(Number);
    return new Date(y, m - 1, d);
  }

  // Safe: YYYY-MM-DD string → "07 Nov 2025" (no timezone shift)
  static formatDisplay(isoString: string): string {
    const [y, m, d] = isoString.split('-').map(Number);
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    const day = String(d).padStart(2, '0');
    return `${day} ${months[m - 1]} ${y}`;
  }

  // Additional helper for display in dialogs
  static formatDate(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    return `${day} ${months[date.getMonth()]} ${date.getFullYear()}`;
  }
}